<script>

    import Toggle from "svelte-toggle";

    export let title;
    export let body;
    export let toggled;
    export let onToggle;
    export let disabled = false;

</script>

<div class="container">
    <div class="content mb-4">
        <h1 class="text-2xl font-bold ">{title}</h1>
        <p class="text-base">{body}</p>
    </div>
    <div class="mr-5">
        <Toggle label="" bind:toggled on:toggle={onToggle} disabled="{disabled}"/>
    </div>
</div>

<style lang="scss">
    .container {
        display: flex;
        justify-content: space-between;
        align-items: flex-start; /* Align items at the start of the cross axis (top) */
    }

    .content {
        flex: 1; /* Take up remaining space */
        padding-left: 3vw;
        color: black;
    }

</style>