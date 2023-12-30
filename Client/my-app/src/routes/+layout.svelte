<script>
    import "../app.postcss";
    let title = "AgileAce";
</script>

<head>
    <title>{title}</title>
</head>

<slot />

<style>
  :global(body) {
    margin: 0;
  }
</style>
