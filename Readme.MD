<h1>Oscillation diagram</h1>

<h4>Description</h4>

<p>This little tool allow to calculate the trend of a wave. At the moment you can
choose between damped and absolute harmonic waves.</p>

<p>Here you can calculate the following options:</p>

<ul><li>Amplitude</li>
<li>Speed</li>
<li>Acceleration</li>
<li>kinetic Energy</li>
<li>Potential Energy</li>
<li>cycle duration</li></ul>

<h4>The math behind</h4>
<p>To understand, you will need to handle linear, power functions and differential functions. 
Next you need to know how to do the mathematical derivation 
with the different rules.</p>
<p>For calculating the amplitude of a non damped vibration this function is needed:
<b>s(t)=s*sin(w*t)</b><br>
If you do the derivation the amplitude function s(t) you will get the speed function v(t) which is:<br>
<b>v(t)s*w*cos(w*t)</b><br>
If you do the derivation again you will get the acceleration function a(t) which is:<br>
<b>a(t)=-s*w^2*sin(w*t)</b></p>
<p>The same is with damped vibrations. Here you have another amplitude function but nearly the same deviate rules.
The only problem is, here you have a exponential function so a damping factor of d=0 is not <b>defined!</b><br>
<b>s(t)s*e^(-d*t)*sin(w*t)</b><br><br>
The deviate here are a bit longer so if you want to see it take a look into the code because I`m lazy now :P</p>

<p>To define the angulare frequency I used a specific function which describes it better in reality:<br>
<b>w=√(w*w)-(d*d)</b></p>

<h5>Variables</h5>

<ul><li>s=maximum distance</li>
<li>w=angular frequency</li>
<li>t=time</li>
<li>d=damping factor</li>
<li>s(t)=amplitude at time "t"</li>
<li>v(t)=speed at time "t"</li>
<li>e=Math (E=2,718...)</li></ul>

<h4>The Code</h4>
<p>The tool is created in java with the JavaFX library. It uses the default math class and all 
functions are structured into a separated class.</p>

<h4>Contact</h4>
<p>The tool is available in german, english and soon in french.</p>
<p>If you have any problems, feel free to contact me via:<br>
<b>E-mail: timo@home-skrobanek.de</b><br>
<b>Author: Timo Skrobanek</b></p>