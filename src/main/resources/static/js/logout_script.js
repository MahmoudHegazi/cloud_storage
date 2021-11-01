window.addEventListener('DOMContentLoaded', (event) => {
    // check logout url



    if (window.location.href.includes("login")){
        const logoutMessageContainer = document.querySelector("#errorContainer");

        function deleteOldMessages(){
           const oldLogoutMsgCheck =  document.querySelector("#logoutContainer");
           if (oldLogoutMsgCheck){
              oldLogoutMsgCheck.remove();
           }
        }

        function createLogoutMsg(){
           const newLogoutCont = document.createElement("div");
           const newLogoutClose = document.createElement("button");
           const newLogoutMsg = document.createElement("span");
           newLogoutCont.classList.add("alert","alert-success", "alert-dismissible", "fade", "show");
           newLogoutClose.classList.add("close");
           newLogoutClose.addEventListener("click", ()=>{newLogoutCont.remove();});
           newLogoutClose.innerHTML = "&times;";
           newLogoutCont.id = "logoutContainer";
           newLogoutMsg.id = "logout_message";
           newLogoutMsg.innerText = "You have been logged out";

           newLogoutCont.appendChild(newLogoutClose);
           newLogoutCont.appendChild(newLogoutMsg);
           logoutMessageContainer.appendChild(newLogoutCont);
        }

        // in login page with java and thyemleaf if user logged I display logout form , now to display logout success message I check logout form
        // if not exist so it proven by java it logout and redirect to login so i display message

        if (logoutMessageContainer && window.location.href.includes("?logout=true")){
            let checkLogoutJava = document.querySelector("#logout_form");
            if (!checkLogoutJava){createLogoutMsg();}else{deleteOldMessages();}

        } else {
           deleteOldMessages();
        }

      // if logout redirected display message
    }
});