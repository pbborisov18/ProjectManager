<script>

    import WhiteboardPageComponent from "$lib/components/WhiteboardPageComponent.svelte";
    import {goto} from "$app/navigation";
    import {company} from "$lib/stores.js";
    import {onMount} from "svelte";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($company);
        if (!BURole.businessUnit.whiteboard) {
            getWhiteboard();
        }
    });

    async function getWhiteboard(){
        fetch('http://localhost:8080/company/whiteboard', {
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
                goto("/company/createWhiteboard");
            } else if(response.status === 400){
                //notification
            } else if(response.status === 401){
                //notification
                error = 401;
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if(response.status === 500){
                //notification
            }
        }).catch(error => {
            //server died or something
        });
    }

</script>

{#if BURole?.businessUnit?.whiteboard}
    <WhiteboardPageComponent BURole="{BURole}" currentUrl="{'/company/createWhiteboard'}"/>
{/if}

<style lang="scss">


</style>