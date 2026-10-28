#!/usr/bin/env python3
"""Generate original bundled Android PNG illustration banners from existing repo's mascot.
No network, no runtime dependency: images produced during APK compile."""
from PIL import Image, ImageDraw, ImageFilter, ImageOps
from pathlib import Path
import math, random

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "app/src/main/res/drawable-nodpi"
OUT.mkdir(parents=True, exist_ok=True)
SOURCE = OUT / "study_robot.webp"
palette = [
 ("#0956F1","#722DEB"), ("#0356F0","#10BFEB"),("#1237DE","#A52FE5"),
 ("#0465F0","#AA35F0"),("#006BDA","#33D2F5"),("#123EE0","#4CBBF8"),
 ("#2255F3","#9D3FF1"),("#0B61ED","#933DF3"),("#532FE6","#B741F5"),
 ("#065CF6","#9B38EA"),("#075CCF","#00C8C1"),("#3855E8","#8733EA"),
 ("#2B53E6","#46C2E9"),("#093CDD","#A63DF5"),("#1556F0","#FF4E9C"),
 ("#075EE7","#09C9DB"),("#1555E4","#8B36EC"),("#1435CE","#742EBA"),
 ("#0457DD","#9036E8"),("#0D37B4","#26B5EC")
]
def rgb(h):return tuple(bytes.fromhex(h.lstrip("#")))
def make(page,color1,color2):
 W,H=1000,460
 base=Image.new("RGB",(W,H))
 d=ImageDraw.Draw(base)
 c1,c2=rgb(color1),rgb(color2)
 for y in range(H):
  t=y/(H-1)
  a=tuple(int(c1[i]*(1-t)+c2[i]*t) for i in range(3))
  d.line((0,y,W,y),fill=a)
 # Aurora light strips, star glows and school-inspired cloud layers
 bloom=Image.new("RGBA",(W,H),(0,0,0,0))
 bd=ImageDraw.Draw(bloom,"RGBA")
 bd.ellipse((-250,-350,520,460), fill=(3,222,244,80))
 bd.ellipse((520,-80,1200,600),fill=(190,109,255,92))
 bd.ellipse((330,220,800,580),fill=(235,247,255,33))
 bloom=bloom.filter(ImageFilter.GaussianBlur(48))
 base=Image.alpha_composite(base.convert("RGBA"),bloom)
 design=ImageDraw.Draw(base,"RGBA")
 rng=random.Random(page*499)
 for i in range(21):
  x,y=rng.randrange(20,W-20),rng.randrange(16,H-23)
  radius=rng.randrange(2,9)
  design.ellipse((x-radius,y-radius,x+radius,y+radius), fill=(245,250,255,rng.randrange(70,150)))
  if i%3==0:
   design.line((x-12,y,x+12,y),fill=(225,252,255,90),width=2)
   design.line((x,y-12,x,y+12),fill=(225,252,255,90),width=2)
 # math/science / study decorations (background)
 for i in range(3):
  x=140+i*180; y=100+i*75
  design.arc((x,y,x+115,y+100), 30,290,fill=(255,255,255,35),width=4)
 # notebook and original school building
 design.rounded_rectangle((720,240,917,478),radius=22,fill=(16,44,142,115),outline=(230,249,255,80),width=3)
 design.polygon([(830,213),(932,272),(728,272)],fill=(255,194,87,130))
 for x in (754,800,846,892):
  design.rectangle((x,287,x+19,347),fill=(191,233,255,85))
 # mascot is a reusable existing repo asset, included in binary APK as PNG
 if SOURCE.exists():
  logo=Image.open(SOURCE).convert("RGBA")
  logo.thumbnail((450,410),Image.Resampling.LANCZOS)
  alpha=logo.getchannel("A")
  # Soft mask for backgrounds (not required when original has transparency).
  base.alpha_composite(logo,(W-logo.width-45,max(0,(H-logo.height)//2)))
 # one-pixel safe padding; Android rounds clip in native frame.
 out=OUT/f"ui_hero_{page:02d}.png"
 base.convert("RGB").save(out,optimize=True)
 return out

for number,(a,b) in enumerate(palette,1):
 make(number,a,b)
print("Generated",len(palette),"illustration PNG assets in",OUT)
