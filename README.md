## Bike Tracker

This is an app designed to track the status of Divvy shared bicycles in Chicago,
focusing on several locations of interest, and showing bike status in a mobile-friendly way.

## Live Demo

An instance of the app is live at https://divvy-bike-tracker.herokuapp.com/

## Deploy to Heroku

You can deploy this to your own Heroku account via this button:

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

## Run Locally with Heroku

MacOS:

```
brew tap heroku/brew && brew install heroku
heroku local web -f Procfile
```

Windows:

```
Install via https://devcenter.heroku.com/articles/heroku-cli#download-and-install
heroku local web -f Procfile.windows
```

## Technology

The app uses Spring Boot to power a basic Java webapp. The app downloads a JSON 
file with details on bike and dock status, and formats it for easy use. Bootstrap, 
Font Awesome, and metro-bootstrap styles are then used to present the data
in an intuitive, mobile-friendly way.

The app is deployed to Heroku, but could be deployed almost anywhere.

![Heroku](https://www.vectorlogo.zone/logos/heroku/heroku-icon.svg) ![Spring Boot](https://www.vectorlogo.zone/logos/springio/springio-icon.svg) ![Bootstrap](https://www.vectorlogo.zone/logos/getbootstrap/getbootstrap-icon.svg) ![Font Awesome](https://www.vectorlogo.zone/logos/font-awesome/font-awesome-icon.svg)

- Heroku: https://heroku.com
- Spring Boot: https://spring.io/projects/spring-boot/
- Bootstrap: http://getbootstrap.com/
- Font Awesome: http://fontawesome.io/
- metro-bootstrap: http://talkslab.github.io/metro-bootstrap/

## Contributors

Steven Noto (https://github.com/stevennoto/)
