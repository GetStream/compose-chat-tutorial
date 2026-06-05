# Compose Chat Tutorial Sample

This repository allows you to check the result after completing the [Compose Chat Tutorial](https://getstream.io/chat/compose/tutorial/).

> Not using Compose yet? Check out the [tutorial repo of our XML based UI components](https://github.com/GetStream/android-chat-tutorial) instead.

The project is pre-configured with a shared [Stream](https://getstream.io) account for testing purposes. You can learn more about Stream Chat [here](https://getstream.io/chat/), and then sign up for an account and obtain your own keys [here](https://getstream.io/chat/trial).

## Quick start

1. Clone the repository
2. Open the project in the latest stable version of Android Studio
3. Run the _app_
4. Make sure to check the [Details](#details) section below for the different steps

## Details

The tutorial app consists of two screens:

* `MainActivity`: Shows the list of available channels.
* `ChannelActivity`: Shows the selected channel view, which includes the header, message list, and message input view.

`ChannelActivity` follows the published tutorial step-by-step and includes a colors customization on `ChatTheme`. Three additional `ChannelActivity*` implementations show alternative customization techniques and screen compositions. To try one, point `MainActivity`'s `onChannelClick` at it:

```kotlin
onChannelClick = { channel ->
    startActivity(ChannelActivity4.getIntent(this, channel.cid))
},
```

You can choose from three alternative `ChannelActivity` implementations:

* `ChannelActivity2` - customizes typography via `ChatTheme`
* `ChannelActivity3` - uses bound and stateless components to build the chat screen
* `ChannelActivity4` - uses a custom message composer component
