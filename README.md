

## 💠 16 - 03 - 2021 UPDATE 💠
ACNH Board has been deployed on Heroku. You can now [visit it here](https://acnhboard.herokuapp.com/home). Remember that Heroku puts the servers to sleep and it will take 1-2 minutes to be ready. You can create your own account or log in as:
* username: user
* password: user

since I've created some examples to help you understand how the site works!

*Note: the code files are not yet updated, since I have to make some changes yet, but everything is working up there! :D

# events-app

Hi everybody!! and welcome to ACNH Board. This is a safe portal where you can publish any event taking place in your island, so other people can join you! It's a great opportunity to meet new people and make new friends, don't you think?

In ACNH Board you can also create private events, so you can remember to check turnips prices, preparing a gift for your favourite villagers... ANYTHING

Come with me!

## Home

This is the /home route, the landing page of this website. You have a navigation bar on the top where you can go to the Sign up page, Login page and toggle light or dark mode

Light mode:

![home light mode](/screenshots/scr00b.png "Home light mode")


Dark mode:

![home dark mode](/screenshots/scr00.png "Home dark mode")



## Sign up and Log in

First of all, we have to create our account. 

![sign up](/screenshots/scr01.png "Sign up")

In the /signup route you'll find a form where you have to introduce a unique username, a unique email and a valid password (twice!). It will automatically check that everything is OK, and the button will remain disabled until all data is correct.

Once we create our account, we will be redirected to the Login page

![log in](/screenshots/scr02.png "Log in")


## Bulletin board

This is the place where EVERY public incoming event will be shown. Here you can see the events cards of every event which has started or is planned. You can make your selection with the checkboxes at the top of the page.

![bulletin board](/screenshots/scr04.png "Bulletin board")

Here we can create new events, if you click on the floating button on the bottom-right corner. A "new event" dialog will open:

![new event dialog](/screenshots/scr06.png "New event dialog")

By clicking on any of the event cards we can access to the event's details. This page will be shown differently depending of our role in the event: guest or host.

## Event details

As guest, we can JOIN, change our status as READY, view the ENTRY CODE or LEAVE (these buttons are all conditional, and will be explained later). Also, our guest's details will be shown:

![event details as guest](/screenshots/scr10.png "Event details as guest")

As host, we will be able to CANCEL, OPEN/CLOSE the registration, and add or edit the ENTRY CODE. Also, if any guest is on the visitors list, it will be shown at the bottom of the page. We can view all or filter by status using the checkboxes, and also KICK any guest.

![event details as host](/screenshots/scr09.png "Event details as host")

As host, it's also possible to edit an event (only if it hasn't been cancelled or finished)

## My events

By clicking on your username on the top-right corner, a menu will appear. There you can click on "My events" page. This is the place where you can find PAST events, and PRIVATE ones, only if you have participated as guest or host.

![my events](/screenshots/scr14.png "My events")


# Okay but... how does it work?

Now that we have taken a look at the page, we can go more in deep about the app functionality. It's quite a complex system (because there are many situations and possibilities) but I'll try to be as clear and concise as possible.

Events have five possible event status:
* NOT_STARTED: when the start date has not been reached
* PENDING_CODE: the start date has been reached, BUT the host has not published the entry code
* STARTED: the start date has been reached, AND the host published the entry code
* FINISHED: the end date has been reached OR max number of visitors have gone to the island
* CANCELLED: the host has cancelled the event


Also, they have two visibility status: 
* PUBLIC: only visible by the host (and guests, since an event can be created as public and then changed to private) in MY EVENTS page
* PRIVATE: visible by all in the bulletin-board and home pages while it is not FINISHED nor CANCELLED


Last but not least, events has also three possible REGISTRATION status:
* OPEN: users can join the event
* CLOSED: this happens automatically when the finish date OR the max number of guest is reached. If any guest leaves the event, it will change to OPEN
* CLOSED_BY_HOST: the host has manually closed the registration, so no more users can join. Only the host can re-open the registration. Private events become CLOSED_BY_HOST automatically.

On the other hand, guest have also six possible status:
* REGISTERED: when a guest clicks the JOIN button
* READY: once the event has started, a READY button will appear, click it so the app will know that you are active and will participate in the event
* KICKED_OUT: be careful! if you are not fast enough, the host can decide to kick you from the queue so other people can visit the island. And remember to be respectful!
* VISITING: if we are READY and our queue position is reached, a SHOW ENTRY CODE button will appear. If we click it, our status will change to VISITING (and obviously an entry code will appear, so we can go to the island)
* VISITED: when we complete our visit, we must click the LEAVE button. If we do so, our status will change to VISITED. Note: if we are not VISITING and click the LEAVE button, we will abandon the event and our queue position will be lost.
* CANCELLED: when the host cancels the event and the guest has not completed the visit, it status will change to CANCELLED.


## What have I learned?

This is my first full-stack solo project, so it was difficult to me to find an idea not too challenging but not too simple so I can learn the maximum possible during the first week developing it. Here I'll do a recap, but it will be incomplete for sure:
* How to create a login system using JWT authentication in Spring Boot (I really have to take a closer look into this, but I have learned a lot about it and found it really interesting!)
* How to implement a login system in the front-end, passing the Authentication Bearer token in each request thanks to a HTTP Interceptor
* Using auth guards to restrict the access to specific routes depending on the authentication status. (I'd love to add CanDeactivate guards in the future 💙)
* A lot of CSS tips! seriously, a LOT (I was very new into this). Special mention to my flying footer, finally fixed.
* Toggling between two different themes (yayyyyyyy! background image included <3)
* Using cookies and localStorage to make info persist during the same session. I have to read more about CSRF and XSS attacks though
* Using a composite key with JPA (I didn't event have both tables in the same service, and it worked incredibly fine! ⭐)
* Capturing back-end exception in the front-end and showing the message
* Capturing throwables inside a circuit-breaker to throw a more specific response from the edge-service
* I hate cascades. And they hate me too. 


## What to expect in future updates?

There's a lot that can be added, but I have some essentials in the list:
* Migrating the users table to another service ✔️ DONE!
* Adding a config server ✔️ DONE!
* Deploy (almost ready!)
* Host will be able to invite guests to their events by username
* Search tool: by event name, by host name, by date.
* CanDeactivate guards
* An additional service to send emails
* Email and in-app notifications
* Automatic refresh of the visitor list (and current queue position) while the event is STARTED
* More dialogs to ask for confirmations, since some buttons are not safe
* More stats in the profile section (total visitors in hosted events, mean/max visitors...)
* Possibility to change profile info and delete account
* I am sure that the security can be improved
