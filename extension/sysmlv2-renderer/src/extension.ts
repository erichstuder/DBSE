import * as vscode from 'vscode';
import { exec, spawn } from 'child_process';

// The extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
    const disposable = vscode.commands.registerCommand('sysmlv2-renderer.openPreview', () => {
        const panel = vscode.window.createWebviewPanel(
            'sysmlPreview',
            'SysML Preview',
            vscode.ViewColumn.Beside,
            {}
        );

        let currentDocument: vscode.TextDocument | undefined = vscode.window.activeTextEditor?.document;

        async function updateSVG(document: vscode.TextDocument) {
            // Only update if the document is the one being previewed
            if (!panel.visible || document !== currentDocument) return;
            const content = document.getText().replace(/\r?\n/g, ' ');
            const PARSER_PATH = "/home/vscode/DBSE/extension/parser/build/classes/java/main";
            const INTERACTIVE = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
            const env = { ...process.env, LANG: 'en_US.UTF-8', LC_ALL: 'en_US.UTF-8' };
            const child = spawn('java', [
                "-Dlog4j.configuration=file:/home/vscode/DBSE/extension/sysmlv2-renderer/log4j.properties",
                '-cp', `${PARSER_PATH}:${INTERACTIVE}`,
                'InteractiveParser'
            ], { env });

            let svgStarted = false;
            let svgOutput = '';
            let leftover = '';
            child.stdout.on('data', (data) => {
                let text = leftover + data.toString();
                let lines = text.split(/\r?\n/);
                leftover = lines.pop() ?? '';
                for (const line of lines) {
                    if (!svgStarted && line.startsWith('svg:')) {
                        svgStarted = true;
                        svgOutput += line.substring(4) + '\n';
                    } else if (svgStarted) {
                        svgOutput += line + '\n';
                    }
                }
            });

            console.log(content);

            child.stderr.on('data', (data) => {
                vscode.window.showErrorMessage(`Parser error: ${data.toString()}`);
            });

            child.on('close', () => {
                // If the last chunk didn't end with a newline, process leftover
                if (svgStarted && leftover) {
                    svgOutput += leftover + '\n';
                }
                panel.webview.html = `
                    <html>
                    <body>
                        ${svgOutput}
                    </body>
                    </html>
                `;
            });

            child.stdin.write(content);
            child.stdin.end();
        }

        // Initial render
        if (currentDocument) {
            updateSVG(currentDocument);
        } else {
            vscode.window.showErrorMessage('No active editor with a file to preview.');
        }

        // Listen for changes in the document
        const changeDocDisposable = vscode.workspace.onDidChangeTextDocument(e => {
            if (currentDocument && e.document === currentDocument) {
                updateSVG(e.document);
            }
        });

        // Listen for switching active editor
        const changeEditorDisposable = vscode.window.onDidChangeActiveTextEditor(editor => {
            if (editor && editor.document !== currentDocument) {
                currentDocument = editor.document;
                updateSVG(currentDocument);
            }
        });

        // Clean up listeners when panel is closed
        panel.onDidDispose(() => {
            changeDocDisposable.dispose();
            changeEditorDisposable.dispose();
        });
    });

    context.subscriptions.push(disposable);
}

export function deactivate() {}
