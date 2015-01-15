package lizardSpock;

import java.io.IOException;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * This class mimics the interface of AudioClip, but for use with an application that can be JAR'ed.
 * 
 * @author Sam Scott
 * @version 1.0 (August 9, 2011)
 *
 */
public class ApplicationAudioClip {

	/**
	 * The file name of the sound
	 */
	private String fileName;
	/**
	 * A sound stream for playing/stopping the sound
	 */
	private AudioStream soundStream;
	/**
	 * A data stream for looping/stopping the sound
	 */
	private ContinuousAudioDataStream loopDataStream;

	/**
	 * Creates an audioclip with the given filename
	 * 
	 * @param fileName The full path of the audioclip
	 */
	public ApplicationAudioClip(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Plays this audioclip from the start until the end, or until stop() is called, whichever comes
	 * first. Exceptions are dumped to stderr.
	 */
	public void play()
	{
		try {
			// A new stream must be created each time the clip is played. This means that multiple calls to 
			// this method will overlay the sound multiplt times as each one spawns a new stream-playing thread.
			soundStream = new AudioStream  (getClass().getClassLoader().getResourceAsStream(fileName));
			AudioPlayer.player.start (soundStream);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Loops this audioclip until stop() is called
	 */
	public void loop()
	{
		try {
			// A new data stream must be created with each call to loop. This means that
			// successive calls will overlay the sound multiple times.
			soundStream = new AudioStream  (getClass().getClassLoader().getResourceAsStream(fileName));
			AudioData data = soundStream.getData();
			loopDataStream = new ContinuousAudioDataStream (data);
			AudioPlayer.player.start (loopDataStream);
		}
		catch (IOException e)
		{
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Stops the sound. Should be used sparingly
	 */
	public void stop()
	{
		AudioPlayer.player.stop (soundStream);
		AudioPlayer.player.stop (loopDataStream);
	}
}
