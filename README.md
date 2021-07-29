# Compose Chat Tutorial Sample

This repository allows you to check the result after completing the [Compose Chat Tutorial](https://getstream.io/chat/compose/tutorial/).

The project is pre-configured with a shared [Stream](https://getstream.io) account for testing purposes. You can learn more about Stream Chat [here](https://getstream.io/chat/), and then sign up for an account and obtain your own keys [here](https://getstream.io/chat/trial).

## Quick start

1. Clone the repository
2. Open the project in Android Studio (Arctic  Fox or later)
3. Run the _app_
4. Make sure to check the [Details](#details) section below for the different steps

## Details

The tutorial app consists of two screens:

* `MainActivity`: Shows the list of available channels.
* `MessagesActivity`: Shows the selected channel view, which includes the header, message list, and message input view.

There are a handful of `MessagesActivity` implementations, which correspond to the steps of the tutorial. You can easily swap them by changing the `onItemClick` handler located in `MainActivity`:

```kotlin
onItemClick = { channel ->
    startActivity(MessagesActivity4.getIntent(this, channel.cid))
},
```

You can choose from four different `MessagesActivity` implementations:

* `MessagesActivity` - a basic _Message Screen_ implementation
* `MessagesActivity2` - includes customization of the screen by using `ChatTheme` 
* `MessagesActivity3` - uses bound and stateless components to build the chat screen, with further customization
* `MessagesActivity4` - uses a custom message composer component for extended customization
