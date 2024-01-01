export interface OverlayPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
