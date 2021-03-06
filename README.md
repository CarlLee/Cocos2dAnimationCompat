Cocos2dAnimationCompat
======================

This is a library to use Cocos2d animation sprite sheet as AnimationDrawable in Android SDK.

Usage
----

See demo subproject, here's a simple snippet:

```java
            AssetsFileOpener fileOpener = new AssetsFileOpener(getApplicationContext());
            SpriteSheetParser p = new SpriteSheetParser(fileOpener);
            p.parse("my_sprite_sheet.plist");
            SpriteSheet result = p.getResult();
            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(fileOpener);
            AnimationDrawable animationDrawable = animAdapter.loadAnimation(result, getResources());
            animationDrawable.setOneShot(false);
            iv.setImageDrawable(animationDrawable);
            animationDrawable.start();
```

Change frame rate
----------------
You can set frame rate of the animation by the last parameter of `SpriteSheetAnimationAdapter`'s
Constructor

```java
            int myFrameRate = 60;
            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(fileOpener,
                myFrameRate);
```

alternatively, you can call `setFrameRate(myFrameRate)` explicitly before calling `loadAnimation()`


FileOpener
---------

FileOpener is an interface to abstract file opening actions, so that I can hide those details from
classes that requires reading files. There're 2 implementations for FileOpener:

- AssetsFileOpener, it looks for file in /assets folder of the application
- SimpleFileOpener, it looks for file using standard Java SDK's File API
