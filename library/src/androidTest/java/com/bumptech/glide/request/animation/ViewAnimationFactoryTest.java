package com.bumptech.glide.request.animation;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ViewAnimationFactoryTest {
    private ViewAnimation.AnimationFactory animationFactory;
    private ViewAnimationFactory<Object> factory;

    @Before
    public void setUp() {
        animationFactory = mock(ViewAnimation.AnimationFactory.class);
        factory = new ViewAnimationFactory<Object>(animationFactory);
    }

    @Test
    public void testFactoryReturnsNoAnimationIfFromMemoryCache() {
        GlideAnimation<Object> animation = factory.build(true /*isFromMemoryCache*/, true /*isFirstResource*/);
        assertEquals(NoAnimation.get(), animation);
        verify(animationFactory, never()).build(Robolectric.application);
    }

    @Test
    public void testFactoryReturnsNoAnimationIfNotFirstResource() {
        GlideAnimation<Object> animation = factory.build(false /*isFromMemoryCache*/, false /*isFirstResource*/);
        assertEquals(NoAnimation.get(), animation);
        verify(animationFactory, never()).build(Robolectric.application);
    }

    @Test
    public void testFactoryReturnsActualAnimationIfNotIsFromMemoryCacheAndIsFirstResource() {
        GlideAnimation<Object> glideAnimation = factory.build(false /*isFromMemoryCache*/, true /*isFirstResource*/);

        Animation animation = mock(Animation.class);
        when(animationFactory.build(any(Context.class))).thenReturn(animation);

        GlideAnimation.ViewAdapter adapter = mock(GlideAnimation.ViewAdapter.class);
        View view = mock(View.class);
        when(adapter.getView()).thenReturn(view);
        glideAnimation.animate(new Object(), adapter);

        verify(view).startAnimation(eq(animation));
    }
}