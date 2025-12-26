import * as vscode from 'vscode';
import { exec, spawn } from 'child_process';

// The extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
    // runSysMLParser().then(output => {
    //     console.log("SysML Parser Output:", output);
    // }).catch(error => {
    //     console.error("Error running SysML Parser:", error);
    // });

    const disposable = vscode.commands.registerCommand('sysmlv2-renderer.openPreview', () => {
        const panel = vscode.window.createWebviewPanel(
            'sysmlPreview',
            'SysML Preview',
            vscode.ViewColumn.Beside,
            {}
        );

        const editor = vscode.window.activeTextEditor;
        if (editor) {
            // Get the file content as a single line (no newlines)
            const content = editor.document.getText().replace(/\r?\n/g, ' ');
            const PARSER_PATH = "/home/vscode/DBSE/extension/parser/build/classes/java/main";
            const INTERACTIVE = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
            const env = { ...process.env, LANG: 'en_US.UTF-8', LC_ALL: 'en_US.UTF-8' };
            const child = spawn('java', ['-cp', `${PARSER_PATH}:${INTERACTIVE}`, 'InteractiveParser'], { env });

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
                console.log(svgOutput);
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
        } else {
            vscode.window.showErrorMessage('No active editor with a file to preview.');
        }

        // No escaping needed for SVG output
    });

    context.subscriptions.push(disposable);
}

// function runSysMLParserWithInput(filePath: string): Promise<string> {
//     return new Promise((resolve, reject) => {
//         const PARSER_PATH = "../../../parser/build/classes/java/main";
//         const INTERACTIVE = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
//         const child = spawn('java', ['-cp', `${PARSER_PATH}:${INTERACTIVE}`, 'InteractiveParser']);

//         // Read file and send to InteractiveParser's stdin
//         fs.createReadStream(filePath).pipe(child.stdin);

//         let output = '';
//         child.stdout.on('data', (data) => {
//             output += data.toString();
//         });

//         child.stderr.on('data', (data) => {
//             reject(data.toString());
//         });

//         child.on('close', () => {
//             resolve(output);
//         });
//     });
// }

function runSysMLParser(): Promise<string> {
    return new Promise((resolve, reject) => {
        const PARSER_PATH = "/home/vscode/DBSE/extension/parser/build/classes/java/main";
        const INTERACTIVE = "/SysML-v2-Pilot-Implementation/org.omg.sysml.interactive/target/org.omg.sysml.interactive-0.55.0-SNAPSHOT-all.jar";
        exec(`java -cp "${PARSER_PATH}:${INTERACTIVE}" InteractiveParser`, (error, stdout, stderr) => {
            if (error) {
                reject(stderr || error.message);
            } else {
                resolve(stdout);
            }
        });
    });
}

function getWebviewContent(): string {
    return `
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>SysML Preview</title>
    </head>
    <body>
        <h1>SysML Preview</h1>
        <svg width="200" height="200" viewBox="0 0 200 200">
            <circle cx="100" cy="100" r="80" stroke="black" stroke-width="4" fill="lightblue" />
            <text x="100" y="110" font-size="30" text-anchor="middle" fill="black">SVG</text>
        </svg>
    </body>
    </html>
    `;
}

export function deactivate() {}
