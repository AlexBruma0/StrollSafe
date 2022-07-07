# Stroll Safe - A CMPT 276 Summer 2022 Project
Stroll Safe is a location tracking application designed with persons with dementia (PWDs) in mind. The 
application will be mainly used by the caregiver that is taking care of the person with dementia.
The application will allow the caregiver to setup safe zones that will activate and de-activate 
based on time and date. When the person with dementia leaves an active safe zone the caregiver will 
be alerted via push notification on their device. The application will use geo-fencing to determine 
if the person with dementia's device is within the specified device.

The application will also include other companion features such as:
- Low battery notifications to both the person with dementia and caregiver's phone
- Idle location notifications to both the person with dementia and caregiver's phone
- An SOS feature for the persons with dementia that can be configured to either call emergency 
services or the caregiver
- Health information for the person with dementia in case they need medical services while without a
caregiver

#Getting Started
These instructions will allow you to have a local copy of the application on your device, which can
be ran with the help of Android Studio.

###Downloading
To download the application clone the reposition and import it into Android Studio.

###Running The Application
You can then can build the project and run it on an Android Virtual Device running Android 7 or 
later, or on a physical device with developer mode enabled running Android 7 or later.


#Database
For Stroll Safe we have chosen the NoSQL database solution as it offers us more flexibility of 
storing and transferring non-primitive data types. MongoDB's Realm and Atlas will serve as the 
backend of Stroll Safe as it offers us the ability to sync data across devices easily and keep local 
copies should there be a break in network connectivity. The following diagram shows how the data is 
handled by MongoDB:

![Alt Text](https://webassets.mongodb.com/_com_assets/cms/realm_animation_slow2x-xq4zpqsi2z.gif)

#Troubleshooting
Majority of the issues can be resolved by simply following these two steps:
```
1. Build -> Clean Project 
2. Build -> Rebuild Project
```


