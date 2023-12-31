<script>

    import {goto} from "$app/navigation";
    import checkIcon from "$lib/images/check.png";
    import deleteIcon from "$lib/images/delete.png";
    import {userEmail, loggedIn} from "$lib/stores";

    export let invite;
    export let onDestroy;

    function declineOrDeleteInvite() {
        if(invite.state === "PENDING") {
            declineInvite();
        } else {
            deleteInvite();
        }
    }

    function declineInvite(){
        let updatedInvite = {
            ...invite,
            state: "DECLINED"
        };
        sendRequest(updatedInvite);
    }

    function acceptInvite(){
        let updatedInvite = {
            ...invite,
            state: "ACCEPTED"
        };
        sendRequest(updatedInvite);
    }

    function sendRequest(updatedInvite){
        fetch('http://localhost:8080/invites', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedInvite),
            credentials: "include"
        })
            .then(response => {
                if (response.ok) {
                    //Show the user that everything was successful
                    if(updatedInvite.state === "ACCEPTED"){
                        //Update the screen informing the user that the invite was accepted
                    } else if (updatedInvite.state === "DECLINED"){
                        //Update the screen informing the user that the invite was declined
                    }
                    //Remove component after is reacted with
                    onDestroy();
                } else if(response.status === 400){
                    //notification
                } else if(response.status === 401){
                    userEmail.set("");
                    loggedIn.set("");
                    goto("/login");
                } else if(response.status === 403){
                    alert("No permission");
                } else if(response.status === 500){
                    //notification
                }
            }).catch(error => {
            console.error(error);
        });
    }

    function deleteInvite() {
        fetch('http://localhost:8080/invites', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(invite),
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                //Show the user that everything was successful
                //Remove component after is reacted with
                onDestroy();
            } else if(response.status === 400){
                //notification
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                //notification
            }
        }).catch(error => {
            //Server's dead or something
        });
    }


</script>

<div class="block">
    <span>You are invited in {invite.businessUnit.name}</span>
    {#if invite.state === "PENDING"}
        <div style="border-left:1px solid #BBBBBB;height:80%"></div>
        <div class="imageDivs clickable" on:click={acceptInvite}>
            <img src="{checkIcon}" alt="" draggable="false" >
        </div>
    {/if}
    <div style="border-left:1px solid #BBBBBB;height:80%"></div>
    <div class="imageDivs clickable" on:click={declineOrDeleteInvite}>
        <img class="xImage" src="{deleteIcon}" alt="" draggable="false" >
    </div>
</div>

<style lang="scss">
    img{
        width: 50px;
    }

    .clickable{
        cursor: pointer;
    }

    .block{
      width: 97vw;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center; /* align items vertically */
      border: 1px solid #BBBBBB;
      min-height: 8vh;
      background-color: #e7e7e7;

      span {
        flex-basis: 65%;
        flex-grow: 1;
        white-space: nowrap; /* prevent text from wrapping */
        overflow: hidden; /* hide overflow */
        text-overflow: ellipsis; /* show ellipsis for truncated text */
        font-family: Bahnschrift, monospace;
        height: 100%;
        font-size: 35px;
        display: inline-flex;
        align-items: center;
        vertical-align: middle;
        padding-left: 1.5vw;
      }

      .imageDivs {
        flex-basis: calc((100% - 65%) / 4);
        flex-grow: 0;
        max-width: 20%;
        min-width: 5%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }

      img {
        max-width: 40px;
        max-height: 40px;
      }

      .xImage{
        max-width: 35px;
        max-height: 35px;
      }

    }

</style>