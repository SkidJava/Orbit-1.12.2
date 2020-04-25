package client.utils.render;

import client.utils.TimeHelper;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ParticleGenerator {
    private int width;
    private int height;
    public ArrayList<Particle> particles;
    private Random random;
    int state;
    int a;
    int r;
    int g;
    int b;

    public ParticleGenerator(int count, int width, int height)
    {
        this.particles = new ArrayList();
        this.random = new Random();
        this.state = 0;
        this.a = 255;
        this.r = 255;
        this.g = 0;
        this.b = 0;
        this.width = width;
        this.height = height;
        for (int i = 0; i < count; i++) {
            this.particles.add(new Particle(this.random.nextInt(width), (int)genRandom(height - 30, height + 100)));
        }
    }

    public void drawParticles(int width, int height)
    {
        this.width = width;
        this.height = height;
        for (Particle p : this.particles)
        {
            if (p.reset)
            {
                p.resetPosSize();
                p.reset = false;
            }
            p.draw();
        }
    }

    public float genRandom(float min, float max)
    {
        return (float)(min + Math.random() * (max - min + 1.0F));
    }

    public class Particle
    {
        private int x;
        private float y;
        private float addY;
        private float size;
        private boolean reset;
        private Random random;
        private TimeHelper fpsTimeHelper = new TimeHelper();

        public Particle(int x, int y)
        {
            this.random = new Random();
            this.x = x;
            this.y = y;
            this.addY = genRandom(0.02f, 0.05f);
            this.size = genRandom(1.0F, 2.0F);

            fpsTimeHelper.reset();
        }

        public void draw()
        {

            if(y <= 0) reset = true;
            y -= addY * (fpsTimeHelper.getCurrentMS() - fpsTimeHelper.getLastMS());
            RenderUtils.drawFullCircle(this.x, this.y, this.size, new Color(255, 255, 255, 200).getRGB());

            fpsTimeHelper.reset();
        }

        public void resetPosSize()
        {
            fpsTimeHelper.reset();

            this.x = this.random.nextInt(width);
            this.y = height;
            this.addY = genRandom(0.02f, 0.05f);
            this.size = genRandom(1.0F, 2.0F);
        }

        public float genRandom(float min, float max)
        {
            Random r = new Random();
            float random = min + r.nextFloat() * (max - min);

            return random;
        }
    }
}
