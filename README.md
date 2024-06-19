++++++++++++++++++++++++++++ Screen Share Using Vonage Api  ++++++++++++++++++++++++++++

Steps to use:

Step 1: Add maven { url 'https://jitpack.io' } in build.gradle(project level) or in settings.gradle

Step 2: implementation 'com.github.bharatKumawatds:ScreenSharing-Vonage:1.0.0' in build.gradle(Module level)

![Screenshot 2024-06-19 at 3 19 01 PM](https://github.com/bharatKumawatds/ScreenSharing-Vonage/assets/172746681/387b6a0c-31f8-4793-a7dd-ee0746a4453e)

dependencies {
	        implementation 'com.github.bharatKumawatds:ScreenSharing-Vonage:1.0.0'
	}

Step 3: create an instance of screen share class and initialize builder on oncreate() method like this 



shareScreen = 

ShareScreen.Builder()
.activity(this@MainActivity)
            
.context(this)
           
.contentView(main_content!!)
            
.API_KEY("47934831")
            
.SESSION_ID("1_MX40NzkxMzgzMX5-MTcxODI2MTQ5OTk4N35OOHVEVTZnb1BNQ0dUUWFRdmVRdTFOaDB-fn4")    

.TOKEN("T1==cGFydG5lcl9pZD00LzkxOzgzMSZzaWc9ZGVjNzIyMmU1NzI3ZWUyMzRhZTJmNjAxNjVhOTQ4Y2FkMGI5MTA0MzpzZXNzaW9uX2lkPTFfTVg0ME56a3hNemd6TVg1LU1UY3hPREkyTVRRNU9UazROMzVPT0hWRVZUWm5iM05CUTBkVVVXRlJkbVZSZFRGT2FEQi1mbjQmY3JlYXRlX3RpbWU9MTcxODI2MTUwMCZub25jZT0wLjc4MzQ1Mjg3OTI1Mjg3Nzcmcm9sZT1tb2RlcmF0b3ImZXhwaXJlX3RpbWU9MTcyMDg1MzUwMCZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ==")
            .build()
        
        shareScreen!!.initializeShareScreen()
        

Note: ".contentView(main_content!!)" pass your main view to capture screen sharing and generate  API_KEY,SESSION_ID & TOKEN first Create OpenTok Account ([https://ui.idp.vonage.com/ui/auth/registration](https://id.tokbox.com/login?response_type=code&redirect_uri=https%3A%2F%2Ftokbox.com%2Faccount%2Fauth%2Fprovider%2Fcallback&state=eyJyZWRpcmVjdCI6Ii8ifQ%3D%3D&client_id=acountportalprod)) & you can check your session on Video playground also (https://tokbox.com/developer/tools/playground/)     


Step 4: Connect or Disconnect session on Resume() and Pause() method like that:
    
    
    override fun onResume() {
        super.onResume()
        shareScreen!!.connectSession()

    }

    override fun onPause() {
        super.onPause()
        shareScreen!!.disconnectSession()
    }




![Screenshot 2024-06-17 at 7 06 22 PM](https://github.com/bharatKumawatds/ScreenSharing-Vonage-/assets/172746681/26ac4046-8a52-41be-81a5-d4ccc68032dd)
