<script>
    import CreateWhiteboardPageComponent from "$lib/components/CreateWhiteboardPageComponent.svelte";
    import {onMount} from "svelte";
    import {company} from "$lib/stores.js";

    let BURole;

    onMount(() => {
        BURole = JSON.parse($company);
    });

</script>

{#if BURole?.businessUnit?.name}
    <CreateWhiteboardPageComponent BURole="{BURole}"/>
{/if}
<style lang="scss">

</style>