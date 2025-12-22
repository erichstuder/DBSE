import * as vscode from 'vscode';

// The extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
    const disposable = vscode.commands.registerCommand('sysmlv2-renderer.openPreview', () => {
        const panel = vscode.window.createWebviewPanel(
            'sysmlPreview',
            'SysML Preview',
            vscode.ViewColumn.Beside,
            {}
        );

        panel.webview.html = getWebviewContent();
    });

    context.subscriptions.push(disposable);
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
