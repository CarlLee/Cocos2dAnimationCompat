Cocos2dAnimationCompat
======================

This is a library to use Cocos2d animation sprite sheet as AnimationDrawable in Android SDK.

Usage
----

See demo subproject, here's a simple snippet:

            SpriteSheet result = p.getResult();
            SpriteSheetAnimationAdapter animAdapter = new SpriteSheetAnimationAdapter(getResources(),
                    result, fileOpener);
            AnimationDrawable animationDrawable = animAdapter.loadAnimation();
            animationDrawable.setOneShot(false);
            iv.setImageDrawable(animationDrawable);
            animationDrawable.start();

FileOpener
---------

FileOpener is an interface to abstract file opening actions, so that I can hide those details from
classes that requires reading files. There're 2 implementations for FileOpener:

- AssetsFileOpener, it looks for file in /assets folder of the application
- SimpleFileOpener, it looks for file using standard Java SDK's File API
