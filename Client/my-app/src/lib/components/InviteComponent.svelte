<script>

    import {goto} from "$app/navigation";
    import checkIcon from "$lib/images/check.png";
    import deleteIcon from "$lib/images/delete.png";
    export let invite;
    export let onDestroy;

    function declineInvite(){
        //pitai kiril kak the fuck da updatenesh lainata tui che usera da znai che vsichko e top
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
    <span>Поканени сте в {invite.businessUnit.name} от {invite.sender.email}</span>
    <img class="clickable" src="{checkIcon}" alt="" draggable="false" on:click={acceptInvite}>
    <img class="clickable" src="{deleteIcon}" alt="" draggable="false" on:click={declineInvite}>
</div>

<style lang="scss">
    img{
        width: 50px;
    }

    .clickable{
        cursor: pointer;
    }

    div{
        background-color: orange;
        width: 97vw;
        display: flex;
        flex-direction: row;
    }

</style>