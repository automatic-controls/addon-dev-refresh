# AddonDevRefresher

WebCTRL is a trademark of Automated Logic Corporation. Any other trademarks mentioned herein are the property of their respective owners.

## About

This WebCTRL add-on [(download link)](https://github.com/automatic-controls/addon-dev-refresh/releases/latest/download/AddonDevRefresher.addon) streamlines the development of other add-ons. Every 3 seconds, it checks for `*.update` files in the `./addons` folder of your WebCTRL server. If an `.update` file is found, it is renamed with extension `.addon`, replacing any existing `.addon` file. Add-ons are automatically disabled and re-enabled during updates.

It is recommended to use this add-on in your local development WebCTRL server. Then have your build script copy in-development add-ons to the `./addons` folder with an `.update` extension whenever your `deploy` task runs. To detect if deployment is successful, your build script should wait for the `.update` file to disappear.

See [./deploy.bat](./deploy.bat) for an extension you can use with <https://github.com/automatic-controls/addon-dev-script>. If your WebCTRL server requires signed add-ons, download [*ACES.cer*](https://github.com/automatic-controls/addon-dev-script/blob/main/ACES.cer?raw=true) for authentication.