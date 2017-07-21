This is an example project, to show how to setup a camunda spring boot application with Gradle.

Requires a Gradle installation. If you are on a Mac and use Homebrew, just install with `brew install gradle`.

It is designed for camunda enterprise edition. To access the camunda enterprise repo, you need to specify the following properties in your `$GRADLE_USER_HOME/gradle.properties`:

* camundaRepoUser
* camundaRepoPassword

Run from the command line with `gradle bootRun`.