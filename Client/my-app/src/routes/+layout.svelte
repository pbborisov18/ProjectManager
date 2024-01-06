<script>
    import "../app.postcss";
    import {Toaster} from "svelte-french-toast";

    let title = "AgileAce";


</script>

<Toaster position="bottom-right"/>

<head>
    <title>{title}</title>
</head>

<slot />

<style>
  :global(body) {
    margin: 0;
  }
</style>
