<script>

    import {onMount} from "svelte";
    import WhiteboardPageComponent from "$lib/components/WhiteboardPageComponent.svelte";
    import {loggedIn, team, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import {getToastStore} from "@skeletonlabs/skeleton";

    let BURole;
    const toastStore = getToastStore();

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
                toastStore.trigger({
                    message: "No whiteboard found!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-yellow-200 rounded-lg border-2 border-yellow-300'
                });
                goto("/company/project/team/createWhiteboard");
            } else if(response.status === 400){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Bad request!",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toastStore.trigger({
                    message: "No permission!",
                    timeout: 3000,
                    hoverable: true,
                    background: 'bg-red-200 rounded-lg border-2 border-red-300'
                });
            } else if(response.status === 500){
                response.text().then(data => {
                    toastStore.trigger({
                        message: "Something went wrong",
                        timeout: 3000,
                        hoverable: true,
                        background: 'bg-red-200 rounded-lg border-2 border-red-300'
                    });
                });
            }
        }).catch(error => {
            toastStore.trigger({
                message: "Server is offline!",
                timeout: 3000,
                hoverable: true,
                background: 'bg-red-200 rounded-lg border-2 border-red-300'
            });
        });
    }

</script>

{#if BURole?.businessUnit?.whiteboard}
    <WhiteboardPageComponent BURole="{BURole}" currentUrl="{'/'}"/>
{/if}

<style lang="scss">

</style>