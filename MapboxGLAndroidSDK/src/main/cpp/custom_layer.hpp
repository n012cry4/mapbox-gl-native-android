#pragma once

#include <array>

// CustomLayerRenderParameters and CustomLayerHost represent public C++ API for the CustomLayer
// See more details https://github.com/mapbox/mapbox-gl-native/blob/master/include/mbgl/gl/custom_layer.hpp

namespace mbgl {
namespace style {

/**
 * Parameters that define the current camera position for a `CustomLayerHost::render()` function.
 */
struct CustomLayerRenderParameters {
    double width;
    double height;
    double latitude;
    double longitude;
    double zoom;
    double bearing;
    double pitch;
    double fieldOfView;
    std::array<double, 16> projectionMatrix;
};

class CustomLayerHost {
public:
    virtual ~CustomLayerHost() = default;

    /**
     * Initialize any GL state needed by the custom layer. This method is called once, from the
     * main thread, at a point when the GL context is active but before rendering for the first
     * time.
     *
     * Resources that are acquired in this method must be released in the `deinitialize` function.
     */
    virtual void initialize() = 0;

    /**
     * Render the layer. This method is called once per frame. The implementation should not make
     * any assumptions about the GL state (other than that the correct context is active). It may
     * make changes to the state, and is not required to reset values such as the depth mask, stencil
     * mask, and corresponding test flags to their original values.
     * Make sure that you are drawing your fragments with a z value of 1 to take advantage of the
     * opaque fragment culling in case there are opaque layers above your custom layer.
     */
    virtual void render(const CustomLayerRenderParameters &) = 0;

    /**
     * Called when the system has destroyed the underlying GL context. The
     * `deinitialize` function will not be called in this case, however
     * `initialize` will be called instead to prepare for a new render.
     *
     */
    virtual void contextLost() = 0;

    /**
     * Destroy any GL state needed by the custom layer, and deallocate context, if necessary. This
     * method is called once, from the main thread, at a point when the GL context is active.
     *
     * Note that it may be called even when the `initialize` function has not been called.
     */
    virtual void deinitialize() = 0;
};

}
}
