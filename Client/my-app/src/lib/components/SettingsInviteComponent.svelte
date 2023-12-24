<script>
    import {Button, CloseButton, Input, Listgroup} from "flowbite-svelte";
    import {goto} from "$app/navigation";
    import {onMount} from "svelte";

    export let BURole;

    let inviteeEmail;
    let alreadyInvited = [];

    function invitePersonToCompany(){
        fetch('http://localhost:8080/company/invite', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({businessUnitDTO: BURole.businessUnit,
                userNoPassDTO: {id:null, email: inviteeEmail}}),
            credentials: "include"
        }).then(response=>{
            if (response.status === 201) {
                //TODO: Bad idea. Should just return the value from the backend when it's created
                getAllInvitesByCompany();
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

        inviteeEmail = "";
    }

    function getAllInvitesByCompany(){
        fetch('http://localhost:8080/businessUnit/invites', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response=>{
            if (response.status === 200) {
                response.json().then(data => {
                    alreadyInvited = data.filter(obj => obj.state === 'PENDING')
                });
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

    // Awful idea as the component gets re-rendered pretty much every time the user clicks the invite "tab"
    onMount(() =>{
        getAllInvitesByCompany();
    });

    let clickedInvite;

    function cancelInvite(){
        if(clickedInvite) {
            clickedInvite = {   ...clickedInvite,
                state:"CANCELLED"}

            fetch("http://localhost:8080/invites", {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(clickedInvite),
                credentials: "include"
            }).then(response=>{
                if (response.status === 200) {
                    alreadyInvited = alreadyInvited.filter(invite => invite.id !== clickedInvite.id);
                } else if(response.status === 400){
                    response.text().then(text => {
                        throw new Error(text);
                    })
                } else if(response.status === 401){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                    goto("/login");
                } else if(response.status === 500){
                    response.text().then(text => {
                        throw new Error(text);
                    });
                }
            }).catch(error => {
                console.error(error);
            });
        }
    }

</script>
<!--TODO: Awful UI and UX as usual-->
<form>
    <div class="grid gap-6 mb-6 md:grid-cols-1 ml-10 mt-5 mr-10">

        {#if alreadyInvited.length > 0}
            <div class="invited text-black">
                <span>Invited people</span>
                <Listgroup items="{alreadyInvited}" let:item>
                    <div class="parent text-black">
                        <div class="text">
                            {item.receiver.email}
                        </div>
                        <CloseButton class="close-button" on:click={() => {
                                clickedInvite = item;
                                cancelInvite();
                            }}/>
                    </div>
                </Listgroup>
            </div>
        {/if}
        <div class="flex flex-col text-black">
            <span>Email invite to</span>
            <Input  type="text" id="projectName" required bind:value={inviteeEmail}/>
        </div>


        <Button color="blue" type="submit" on:click={invitePersonToCompany}>Send</Button>
    </div>
</form>

<style lang="scss">

    .invited{
        max-height: 40vh;
        overflow-y: auto;
    }

    .text{
        text-align: center;
    }

    .close-button{
        position: absolute;
        top: 0;
        right: 0;
        z-index: 1;
    }

    .parent{
        position: relative;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center; /* Align child elements horizontally */
        min-width: fit-content;
    }

</style>