import * as vscode from 'vscode';

// The extension is activated the very first time the command is executed
export function activate(context: vscode.ExtensionContext) {
    // Use the console to output diagnostic information (console.log) and errors (console.error)
    console.log('Congratulations, your extension "sysmlv2-renderer" is now active!');

    // Note: The command has been defined in the package.json file
    const disposable = vscode.commands.registerCommand('sysmlv2-renderer.openPreview', () => {
        vscode.window.showInformationMessage('Now it is time to implement sysmlv2-renderer preview!');
    });

    context.subscriptions.push(disposable);
}

export function deactivate() {}
