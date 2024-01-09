# Shopping-App 🛍

<img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/a6f34238-a018-4493-8ddd-2fe5d530d565" width="192" height="192" />

Shopping App is a fictional shopping application. To access the application, an account needs to be created. The main screen displays various products. Products can be filtered based on categories. In the search screen, you can search for any desired product. You can add your desired product to favorites and quickly access it from the favorites screen. In the profile screen, you can update your username, phone number, address, profile picture, and date of birth.

The application uses [Firebase Authentication](https://firebase.google.com/docs/auth) for user verification. Additionally, a phone number can be added. When adding a phone number, a verification code is send to the added phone number. After entering the received code in the application, the phone number gets associated with the account. User profile images are stored using [Firebase Storage](https://firebase.google.com/docs/storage). The user's address and date of birth information are stored using [Cloud Firestore](https://firebase.google.com/docs/firestore).

A new Firebase application has been created to store user addresses and date of birth information using Cloud Firestore. To learn how multiple Firebase applications are used within a project, [Configure multiple projects in your application.](https://firebase.google.com/docs/projects/multiprojects?hl=en#use_multiple_projects_in_your_application)

### <b>Setting up the second Firebase application</b> 🛠

Enter the required information in the local.properties file as specified in the provided link.

<img src="https://github.com/AhmetOcak/JetpackCompose-Shopping-App/assets/73544434/e5c89c09-5101-48a6-92d4-64544cfee748" />

Creating the instance for the second Firebase application. (di/AppModule)

<img src="https://github.com/AhmetOcak/JetpackCompose-Shopping-App/assets/73544434/15300f35-ab66-4909-b51f-7f974d4bd7c9" />
 
## Tech Stack 📚

* [Android Architecture Components](https://developer.android.com/topic/architecture)
    * [Navigation](https://developer.android.com/guide/navigation)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Repository](https://developer.android.com/topic/architecture/data-layer?hl=en)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [Room](https://developer.android.com/training/data-storage/room)
* [Retrofit](https://github.com/square/retrofit)
* [Coil](https://github.com/coil-kt/coil)
* [Firebase Auth](https://firebase.google.com/docs/auth)
* [Firebase Storage](https://firebase.google.com/docs/storage?hl=en)
* [Firebase Firestore](https://firebase.google.com/docs/firestore?hl=en)
* [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging)
* [Image Cropper](https://github.com/mr0xf00/easycrop)

## Outputs 🖼

|                  | Light | Dark |
|------------------|-------|------|
| Login Screen     | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/df2fb363-8c9f-43f5-9bf0-8cece45dc53f" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/96b0287f-2be4-4833-b370-6e530db25269" width="240" height="480"/>     |
| Register Screen  | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/dbb4c507-1a72-4522-bfcd-d39487c64b86" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/f9cecddf-631c-4fc4-9e81-b7f19a90e439" width="240" height="480"/>     |
| Home Screen      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/ca52a7b5-77a9-4659-9edc-4e31cbffa478" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/147d755a-2460-4197-b740-392c306c1dac" width="240" height="480"/>     |
| Search Screen    | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/d0726a15-e5ab-4f1a-9338-c429b1c0b68b" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/9e2b2b16-30e2-4240-af1f-f8e8251004b7" width="240" height="480"/>     |
| Favorites Screen | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/5c4cd695-99fa-403f-9f43-b11df6b5a179" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/0f263869-03d9-48e9-b528-817e4b43f322" width="240" height="480"/>     |
| Profile Screen   | <img src="https://github.com/AhmetOcak/JetpackCompose-Shopping-App/assets/73544434/1abe7eeb-7dcf-4b9d-b409-507626b1066d" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/JetpackCompose-Shopping-App/assets/73544434/0826544c-f619-452b-98fb-63866b2afb25" width="240" height="480"/>     |
| Product Screen   | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/829178f7-5817-44c3-9fc0-f50a0dd28280" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/ac6eb116-4f53-428d-a3cf-b43caa2c8abc" width="240" height="480"/>     |
| Cart Screen      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/5153dc58-4b83-45d8-847f-dc3fed2951de" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/d0ef5cf2-7206-4e0a-9f3d-9d48240c8f51" width="240" height="480"/>     |
| Payment Screen   | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/a79dd356-852e-44b8-bf2c-03d0d58061ee" width="240" height="480"/>      | <img src="https://github.com/AhmetOcak/ShoppingApp/assets/73544434/098d3741-32f9-4ee7-801e-cf56a85de79e" width="240" height="480"/>     |

## Architecture 🏗
The app uses MVVM [Model-View-ViewModel] architecture to have a unidirectional flow of data, separation of concern, testability, and a lot more.

![mvvm](https://user-images.githubusercontent.com/73544434/197416569-d42a6bbe-126e-4776-9c8f-2791925f738c.png)

## API 📦
[Fake store API](https://fakestoreapi.com/)

