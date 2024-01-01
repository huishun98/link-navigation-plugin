import { WebPlugin } from '@capacitor/core';

import type { OverlayPlugin } from './definitions';

export class OverlayWeb extends WebPlugin implements OverlayPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
