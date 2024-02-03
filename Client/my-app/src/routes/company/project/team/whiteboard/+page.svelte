<script>

    import {onMount} from "svelte";
    import WhiteboardPageComponent from "$lib/components/WhiteboardPageComponent.svelte";
    import {loggedIn, team, userEmail} from "$lib/stores.js";
    import {goto} from "$app/navigation";
    import toast from "svelte-french-toast";
    import {PUBLIC_BACKEND_URL} from "$lib/Env.js";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($team);
        if(!BURole.businessUnit.whiteboard){
            getWhiteboard();
        }
    })

    async function getWhiteboard(){
        fetch(PUBLIC_BACKEND_URL + 'whiteboard', {
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
                toast.error("No whiteboard found!");
                goto("/company/project/team/createWhiteboard");
            } else if(response.status === 400){
                response.text().then(data => {
                    toast.error("Bad request!");
                });
            } else if(response.status === 401){
                userEmail.set("");
                loggedIn.set("");
                goto("/login");
            } else if (response.status === 403){
                toast.error("No permission!");
            } else if(response.status === 500){
                response.text().then(data => {
                    toast.error("Something went wrong!");
                });
            }
        }).catch(error => {
            toast.error("Server is offline!");
        });
    }

</script>

{#if BURole?.businessUnit?.whiteboard}
    <WhiteboardPageComponent BURole="{BURole}" currentUrl="{'/'}"/>
{/if}

<style lang="scss">

</style>