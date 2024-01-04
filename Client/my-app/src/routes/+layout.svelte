<script>
    import {initializeStores, Toast} from '@skeletonlabs/skeleton';
    import "../app.postcss";

    let title = "AgileAce";

    initializeStores();

</script>

<Toast position="br" class="rounded-lg"/>


<head>
    <title>{title}</title>
</head>

<slot />

<style>
  :global(body) {
    margin: 0;
  }
</style>
