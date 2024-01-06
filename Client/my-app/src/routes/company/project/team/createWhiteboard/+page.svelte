<script>

    import CreateWhiteboardPageComponent from "$lib/components/CreateWhiteboardPageComponent.svelte";
    import {team} from "$lib/stores.js";
    import {onMount} from "svelte";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($team);
    })
</script>

{#if BURole?.businessUnit?.name}
    <CreateWhiteboardPageComponent BURole={BURole} />
{/if}

<style lang="scss">

</style>