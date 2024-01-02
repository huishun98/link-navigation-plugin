# Link Navigation Plugin for Capacitor

This link navigation plugin for Capacitor creates a simple system alert window (overlay) that draws over other apps and allows user to navigate through the array of links passed into the plugin.

Only works on Android as system alert window functionality is not supported on iOS.

### Additional features
* Show map button: Add [query, longitude, latitude](#interfaces) to the array elements.

## Install

```bash
npm install link-navigation-plugin
npx cap sync
```

## Usage
```typescript
import { Overlay } from 'link-navigation-plugin';

Overlay.open({ 
    values: [
        { name: "Google", url: "https://google.com" },
        { name: "Yahoo", url: "https://yahoo.com" },
    ]},
    package: "com.huishun.narie" // your package name for plugin to redirect back to
)
```

## API

<docgen-index>

* [`open(...)`](#open)
* [`close()`](#close)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### open(...)

```typescript
open(options: { values: Link[]; package: string; }) => Promise<{ value: string; }>
```

| Param         | Type                                              |
| ------------- | ------------------------------------------------- |
| **`options`** | <code>{ values: Link[]; package: string; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### close()

```typescript
close() => Promise<void>
```

--------------------


### Interfaces


#### Link

| Prop               | Type                        |
| ------------------ | --------------------------- |
| **`url`**          | <code>string</code>         |
| **`name`**         | <code>string</code>         |
| **`query`**        | <code>string \| null</code> |
| **`longitude`**    | <code>number \| null</code> |
| **`latitude`**     | <code>number \| null</code> |
| **`displacement`** | <code>number \| null</code> |

</docgen-api>
