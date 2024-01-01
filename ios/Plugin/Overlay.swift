import Foundation

@objc public class Overlay: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
