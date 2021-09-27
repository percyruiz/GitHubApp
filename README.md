# GitHubApp

This application displays a list of GitHub reposities obtained from https://api.github.com/search/repositories.

It has a simple search bar with an additional option to sort by stars, forks, and help-wanted-issues. 
Clicking the elements on the list will redirect the app to the browser and open the chosen repository on GitHub site.

It also has offline caching to show list of repositories from last search and can it caches the last search keyword and sort by.

## Installation
Clone this repository and import into **Android Studio**
```bash
https://github.com/percyruiz/GitHubApp.git
```

## Screenshots
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/134850971-1466dcea-fcfb-4982-b743-b07f87bec07e.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/134851062-729f3895-45ac-4da3-b298-eac4cf86aa7d.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/134851537-62bef0c3-3930-4cd3-9e8b-c5bcc6db7650.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/134851610-51ffab65-44f0-4433-b3ae-01afc53c34ac.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/134851725-efd92b83-132b-4b01-903e-2e124d4555ed.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Architecture
The app uses MVVM architecture which takes advantage of Android Jetpack's Android Architecture Components https://developer.android.com/topic/libraries/architecture.
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/126056079-2c0e8155-2201-45e6-bf3f-514eda1c39ff.png" width="50%" height="50%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Dependencies
- Dependency injection (with [Koin](https://www.koin.com/))
- Reactive programming (with [Kotlin Flows](https://kotlinlang.org/docs/reference/coroutines/flow.html))
- Http client (with [Retrofit](https://square.github.io/retrofit/))
- Paging implementation which handles loading of items and caching to db (with [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview))
- Google [Material Design](https://material.io/blog/android-material-theme-color) library
- Android architecture components

## Maintainers
This project is mantained by:
* [Percival Ruiz](https://github.com/percyruiz)
