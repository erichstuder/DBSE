import * as vscode from 'vscode';
import { spawn } from 'child_process';

// The extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
    const disposable = vscode.commands.registerCommand('sysmlv2-renderer.openPreview', () => {
        const panel = vscode.window.createWebviewPanel(
            'sysmlPreview',
            'SysML Preview',
            vscode.ViewColumn.Beside,
            {}
        );

        panel.webview.html = `
            <html>
            <body>
                <h3>Loading SysML v2 Diagram...</h3>
            </body>
            </html>
        `;

        let currentDocument: vscode.TextDocument | undefined = vscode.window.activeTextEditor?.document;

        const PARSER_PATH = "/home/vscode/DBSE/extension/parser/build/classes/java/main";
        const INTERACTIVE = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
        const env = { ...process.env, LANG: 'en_US.UTF-8', LC_ALL: 'en_US.UTF-8' };
        const child = spawn('java', [
            "-Dlog4j.configuration=file:/home/vscode/DBSE/extension/sysmlv2-renderer/log4j.properties",
            '-cp', `${PARSER_PATH}:${INTERACTIVE}`,
            'InteractiveParser'
        ], { env });

        let svg_started = false;
        let svg_output = '';
        child.stdout.on('data', (data) => {
            let text = data.toString();
            const start_index = text.indexOf('<svg');
            const end_index = text.indexOf('</svg>');
            if (start_index !== -1 && end_index !== -1) {
                svg_started = true;
                svg_output = text.substring(start_index, end_index + 6);
                if (currentDocument) {
                    update_svg(currentDocument);
                }
            }
            else if (start_index !== -1) {
                svg_started = true;
                svg_output = text.substring(start_index);
            }
            else if (end_index !== -1 && svg_started) {
                svg_started = false;
                svg_output += text.substring(0, end_index + 6);
                if (currentDocument) {
                    update_svg(currentDocument);
                }
            }
            else {
                svg_output += text;
            }
        });

        child.stderr.on('data', (data) => {
            vscode.window.showErrorMessage(`Parser error: ${data.toString()}`);
        });

        async function send_to_parser(document: vscode.TextDocument) {
            if (document !== currentDocument) {
                return;
            }

            const textWithoutNewlines = document ? document.getText().replace(/\n/g, '') : '';
            child.stdin.write(textWithoutNewlines + '\n');
        }

        async function update_svg(document: vscode.TextDocument) {
            if (!panel.visible || document !== currentDocument) {
                return;
            }

            panel.webview.html = `
                <html>
                <body>
                    ${svg_output}
                </body>
                </html>
            `;
        }

        send_to_parser(currentDocument!); // Initial send

        const changeDocDisposable = vscode.workspace.onDidChangeTextDocument(e => {
            send_to_parser(currentDocument!);
        });

        const changeEditorDisposable = vscode.window.onDidChangeActiveTextEditor(editor => {
            send_to_parser(currentDocument!);
        });

        panel.onDidDispose(() => {
            changeDocDisposable.dispose();
            changeEditorDisposable.dispose();
            child.stdin.end();
        });
    });

    context.subscriptions.push(disposable);
}

export function deactivate() {}
