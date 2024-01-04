import { join } from 'path';
import { skeleton } from '@skeletonlabs/tw-plugin';

const config = {
  content: [
    "./src/**/*.{html,js,svelte,ts}",
    "./node_modules/flowbite-svelte/**/*.{html,js,svelte,ts}",
      join(require.resolve(
          '@skeletonlabs/skeleton'),
          '../**/*.{html,js,svelte,ts}'
      ),
  ],

  theme: {
    extend: {},
  },

  plugins: [
    require('flowbite/plugin'),
      skeleton
  ],
  darkMode: 'class',
};

module.exports = config;