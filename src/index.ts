import { registerPlugin } from '@capacitor/core';

import type { OverlayPlugin } from './definitions';

const Overlay = registerPlugin<OverlayPlugin>('Overlay', {
  web: () => import('./web').then(m => new m.OverlayWeb()),
});

export * from './definitions';
export { Overlay };
