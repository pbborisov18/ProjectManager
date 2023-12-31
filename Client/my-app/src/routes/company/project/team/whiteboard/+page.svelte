<script>

    import {onMount} from "svelte";
    import WhiteboardPageComponent from "$lib/components/WhiteboardPageComponent.svelte";
    import {loggedIn, team, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($team);
        if(!BURole.businessUnit.whiteboard){
            getWhiteboard();
        }
    })

    async function getWhiteboard(){
        fetch('http://localhost:8080/company/project/team/whiteboard', {
            method: 'POST',
            headers: {
                'Content-Type': "application/json",
            },
            body: JSON.stringify(BURole.businessUnit),
            credentials: "include"
        }).then(response => {
            if(response.status === 200){
                response.json().then(w => {
                    BURole.businessUnit.whiteboard = w;
                });
            } else if(response.status === 204){
                //notification
                goto("/company/project/team/createWhiteboard");
            } else if(response.status === 400){
                //notification
            } else if(response.status === 401){
                //notification
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                alert("No permission");
            } else if(response.status === 500){
                //notification
            }
        }).catch(error => {
            //server died or something
        });
    }

</script>

{#if BURole?.businessUnit?.whiteboard}
    <WhiteboardPageComponent BURole="{BURole}" currentUrl="{'/'}"/>
{/if}

<style lang="scss">

</style>