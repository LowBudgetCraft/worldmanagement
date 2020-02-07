/*
 *
 *  * This file is part of WorldManagement, licensed under the MIT License.
 *  *
 *  *  Copyright (c) crysis992 <crysis992@gmail.com>
 *  *  Copyright (c) contributors
 *  *
 *  *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  *  of this software and associated documentation files (the "Software"), to deal
 *  *  in the Software without restriction, including without limitation the rights
 *  *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  *  copies of the Software, and to permit persons to whom the Software is
 *  *  furnished to do so, subject to the following conditions:
 *  *
 *  *  The above copyright notice and this permission notice shall be included in all
 *  *  copies or substantial portions of the Software.
 *  *
 *  *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  *  SOFTWARE.
 *
 */

package crytec.worldmanagement.data;

import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class UnloadCheckTimer implements Runnable {

  private final Consumer<Boolean> done;
  private final String world;
  private final BukkitTask task;

  public UnloadCheckTimer(JavaPlugin plugin, Consumer<Boolean> consumer, String world) {
    done = consumer;
    this.world = world;
    task = Bukkit.getScheduler().runTaskTimer(plugin, this, 1, 1L);
  }

  @Override
  public void run() {
    if (Bukkit.getWorld(world) == null) {
      done.accept(true);
      task.cancel();
    }
  }
}
