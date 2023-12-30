<script>

    import CreateWhiteboardPageComponent from "$lib/components/CreateWhiteboardPageComponent.svelte";
    import {project} from "$lib/stores.js";
    import {onMount} from "svelte";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($project);
    });

</script>

{#if BURole?.businessUnit?.name}
    <CreateWhiteboardPageComponent BURole="{BURole}"/>
{/if}

<style lang="scss">

</style>