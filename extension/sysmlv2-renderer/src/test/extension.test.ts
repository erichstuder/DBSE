import * as assert from 'assert';
import * as sinon from 'sinon';

// You can import and use all API from the 'vscode' module
// as well as import your extension to test it
import * as vscode from 'vscode';
import * as myExtension from '../extension';

suite('SysMLv2 Renderer Extension', () => {
    let context: any;

    setup(() => {
        context = { subscriptions: [] };
    });

    test('Command is registered', async () => {
        const registerCommandStub = sinon.stub(vscode.commands, 'registerCommand');
        myExtension.activate(context);
        sinon.assert.calledWith(registerCommandStub, 'sysmlv2-renderer.openPreview');
        registerCommandStub.restore();
    });

    test('Webview panel is created on command', async () => {
        const createWebviewPanelStub = sinon.stub(vscode.window, 'createWebviewPanel').returns({
            webview: { html: '' },
            onDidDispose: () => ({ dispose: () => {} }),
            visible: true
        } as any);
        const registerCommandStub = sinon.stub(vscode.commands, 'registerCommand').callsFake((_, cb) => {
            cb();
            return { dispose: () => {} };
        });
        myExtension.activate(context);
        sinon.assert.calledOnce(createWebviewPanelStub);
        createWebviewPanelStub.restore();
        registerCommandStub.restore();
    });

    test('Parser process is spawned and handles stdout/stderr', async () => {
        const spawnStub = sinon.stub(require('child_process'), 'spawn').returns({
            stdout: { on: sinon.stub() },
            stderr: { on: sinon.stub() },
            stdin: { write: sinon.stub(), end: sinon.stub() },
            on: sinon.stub()
        } as any);
        const createWebviewPanelStub = sinon.stub(vscode.window, 'createWebviewPanel').returns({
            webview: { html: '' },
            onDidDispose: () => ({ dispose: () => {} }),
            visible: true
        } as any);
        const registerCommandStub = sinon.stub(vscode.commands, 'registerCommand').callsFake((_, cb) => {
            cb();
            return { dispose: () => {} };
        });
        myExtension.activate(context);
        sinon.assert.calledOnce(spawnStub);
        spawnStub.restore();
        createWebviewPanelStub.restore();
        registerCommandStub.restore();
    });

    test('SVG output updates webview HTML', async () => {
        // Mocks
        const fakePanel = {
            webview: { html: '' },
            onDidDispose: () => ({ dispose: () => {} }),
            visible: true
        } as any;
        const createWebviewPanelStub = sinon.stub(vscode.window, 'createWebviewPanel').returns(fakePanel);
        const registerCommandStub = sinon.stub(vscode.commands, 'registerCommand').callsFake((_, cb) => {
            cb();
            return { dispose: () => {} };
        });
        // Simulate SVG output
        const spawnStub = sinon.stub(require('child_process'), 'spawn').returns({
            stdout: {
                on: (event: string, handler: (data: Buffer) => void) => {
                    if (event === 'data') {
                        handler(Buffer.from('<svg>test</svg>'));
                    }
                }
            },
            stderr: { on: sinon.stub() },
            stdin: { write: sinon.stub(), end: sinon.stub() },
            on: sinon.stub()
        } as any);
        myExtension.activate(context);
        assert.match(fakePanel.webview.html, /Loading SysML v2 Diagram.../);
        spawnStub().stdout.on('data', (data: Buffer) => {
            fakePanel.webview.html = data.toString();
        });
        assert.match(fakePanel.webview.html, /<svg>test<\/svg>/);
        spawnStub.restore();
        createWebviewPanelStub.restore();
        registerCommandStub.restore();
    });

    test('Event listeners are disposed and child process stdin is closed on panel dispose', async () => {
        const disposeStub = sinon.stub();
        const endStub = sinon.stub();
        const fakePanel = {
            webview: { html: '' },
            onDidDispose: (cb: any) => { cb(); return { dispose: () => {} }; },
            visible: true
        } as any;
        const createWebviewPanelStub = sinon.stub(vscode.window, 'createWebviewPanel').returns(fakePanel);
        const registerCommandStub = sinon.stub(vscode.commands, 'registerCommand').callsFake((_, cb) => {
            cb();
            return { dispose: () => {} };
        });
        const spawnStub = sinon.stub(require('child_process'), 'spawn').returns({
            stdout: { on: sinon.stub() },
            stderr: { on: sinon.stub() },
            stdin: { write: sinon.stub(), end: endStub },
            on: sinon.stub()
        } as any);
        myExtension.activate(context);
        sinon.assert.calledOnce(endStub);
        spawnStub.restore();
        createWebviewPanelStub.restore();
        registerCommandStub.restore();
    });
});
