export interface OverlayPlugin {
  open(options: { values: Link[], package: string }): Promise<{ value: string }>;
  close(): Promise<void>;
}

export interface Link {
  url: string
  name: string
  query: string | null
  longitude: number | null
  latitude: number | null
  displacement: number | null
}
