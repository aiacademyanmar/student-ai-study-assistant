#!/usr/bin/env python3
"""Generate 20 distinct, original blue/cyan/purple hero assets for native Android.

Design-aligned, not pixel-perfect replicas of the user's full screen mockups.
The existing bundled study_robot.webp mascot is drawn on a bright white ellipse
at right, leaving the left side available for real native, accessible text.
"""
from PIL import Image, ImageDraw, ImageFilter
from pathlib import Path
import math, random, colorsys

ROOT=Path(__file__).resolve().parents[1]
OUT=ROOT/"app/src/main/res/drawable-nodpi"
OUT.mkdir(parents=True,exist_ok=True)
SOURCE=OUT/"study_robot.webp"
palette=[
("#1767FF","#733FF5"),("#1767FF","#8138EF"),("#2366FF","#14C5EA"),
("#2562F8","#7442F4"),("#115DFA","#A844EC"),("#057AF5","#743FED"),
("#2170FC","#A746EA"),("#1456E7","#7D43F4"),("#155CF4","#8B42F0"),
("#185FF1","#9D39F0"),("#1174EF","#18C9E8"),("#245FF4","#8A45EC"),
("#165EE8","#8052F4"),("#176AFC","#9D42EE"),("#2B60FA","#F34FA0"),
("#0B72E5","#16BBE7"),("#175EE9","#8440EF"),("#1958D9","#9A4CED"),
("#2367F8","#8646F2"),("#174EDA","#16B8E8")
]
icons=["✦","AI","∑","▤","✎","✓","★","AI","100","✦","▣","▶","PDF","✎","◷","▥","✓","⚙","?","AI"]
def rgb(s):return tuple(bytes.fromhex(s.lstrip("#")))
def blend(a,b,t):return tuple(round(a[i]*(1-t)+b[i]*t) for i in range(3))
def make(page,left,right):
 W,H=1000,460
 bg=Image.new("RGBA",(W,H),(*rgb(left),255))
 px=bg.load();a,b=rgb(left),rgb(right)
 for x in range(W):
  t=x/max(1,W-1)
  c=blend(a,b,t)
  for y in range(H):px[x,y]=(*c,255)
 d=ImageDraw.Draw(bg,"RGBA")
 # White circular mascot backdrop, as in the 20-screen UI reference.
 d.ellipse((630,-115,1114,375),fill=(255,255,255,253))
 d.ellipse((664,-80,1072,337),fill=(243,249,255,245))
 # Soft cyan accent rings, intentionally behind illustration.
 d.arc((602,-142,1160,414),160,346,fill=(127,240,255,125),width=7)
 d.arc((602,-142,1160,414),18,140,fill=(234,212,255,135),width=5)
 # Subtle sparkles in the text-safe band at far left.
 rng=random.Random(page*815)
 for i in range(10):
  x=rng.randint(30,590);y=rng.randint(25,H-30);r=rng.choice([2,3,4])
  d.ellipse((x-r,y-r,x+r,y+r),fill=(255,255,255,rng.randint(60,150)))
 # Short study symbols on the right only; never rasterize UI labels.
 for x,y in [(685,90),(950,220)]:
  d.line((x-8,y,x+8,y),fill=(73,217,255,130),width=3)
  d.line((x,y-8,x,y+8),fill=(73,217,255,130),width=3)
 if SOURCE.exists():
  mascot=Image.open(SOURCE).convert("RGBA")
  mascot.thumbnail((395,350),Image.Resampling.LANCZOS)
  # Source is already a composited reusable artwork; alpha composite preserves transparency.
  bx=695+(360-mascot.width)//2
  by=43+(340-mascot.height)//2
  bg.alpha_composite(mascot,(bx,by))
 # V2.37 generated assets must be reproducible and not depend on network.
 out=OUT/f"ui_hero_{page:02d}.png"
 bg.convert("RGB").save(out,optimize=True)
 return out
if __name__=="__main__":
 images=[make(i,*pair) for i,pair in enumerate(palette,1)]
 assert len(images)==20
 for file in images:
  with Image.open(file) as im:
   assert im.size==(1000,460) and im.mode=="RGB"
 print("Generated 20 native hero assets with mascot and white backdrop",OUT)
