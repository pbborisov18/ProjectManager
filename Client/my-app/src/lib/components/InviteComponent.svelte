<script>

    import {goto} from "$app/navigation";
    import checkIcon from "$lib/images/check.png";
    import deleteIcon from "$lib/images/delete.png";
    export let invite;
    export let onDestroy;

    function declineInvite(){
        let updatedInvite = {
            ...invite,
            state: "DECLINED"
        };
        sendRequest(updatedInvite);
    }

    function acceptInvite(){
        //send a request to accept the invite
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
                    //Remove component after is reacted with
                    if(updatedInvite.state === "ACCEPTED"){
                        //Update the screen informing the user that the invite was accepted
                    } else if (updatedInvite.state === "DECLINED"){
                        //Update the screen informing the user that the invite was declined
                    }
                    onDestroy();
                } else if(response.status === 400){
                    response.text().then(text => {
                        throw new Error(text);
                    })
                } else if(response.status === 401){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                    goto("/login");
                } else if(response.status === 403){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                } else if(response.status === 500){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                }
            }).catch(error => {
            console.error(error);
        });
    }

</script>

<div class="block">
    <span>You are invited in {invite.businessUnit.name}</span>
    <div style="border-left:1px solid #BBBBBB;height:80%"></div>
    <div class="imageDivs clickable" on:click={acceptInvite}>
        <img src="{checkIcon}" alt="" draggable="false" >
    </div>
    <div style="border-left:1px solid #BBBBBB;height:80%"></div>
    <div class="imageDivs clickable" on:click={declineInvite}>
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