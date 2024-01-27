import { WebPlugin } from '@capacitor/core';

import type { Link, OverlayPlugin } from './definitions';

export class OverlayWeb extends WebPlugin implements OverlayPlugin {
  async open(options: { values: Link[], package: string }): Promise<{ value: string }> {
    return { value: options.values.length.toString() };
  }

  async close(): Promise<{ lat?: number, lng?: number }> {
    return {}
  }

  async state(): Promise<{ isActive: boolean }> {
    return { isActive: false }
  }
}
