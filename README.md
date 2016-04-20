# EasyDoist
A library to help you with Todoist Oauth as well as work with their API. Currently only supports items for sync. Currently under development.

For Todoist's API documentation, please visit [their developer page](https://developer.todoist.com).

#### Usage
The library is currently distributed via Jitpack. Add the following to your root level build.gradle
  
    allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
	
And the following to your app's build.gradle

    dependencies {
	        compile 'com.github.raveeshbhalla:EasyDoist:0.1.0-SNAPSHOT'
	}
	    
