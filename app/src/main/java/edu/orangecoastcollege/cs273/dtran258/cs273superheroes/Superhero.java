package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

/**
 * Represents a Superhero for the purposes of the CS 273 Superheroes quiz, including the superhero's
 * username, name, super power, one thing, and the file name (including path) for his/her
 * image.
 *
 * @author Derek Tran
 * @version 1.0
 * @since October 15, 2017
 */

public class Superhero
{
    private String mUserName;
    private String mName;
    private String mSuperPower;
    private String mOneThing;
    private String mFileName;

    /**
     * Instantiates a new <code>Superhero</code> given its username, name, superpower, and one
     * thing about them.
     *
     * @param userName   The username of the <code>Superhero</code>.
     * @param name       The name of the <code>Superhero</code>.
     * @param superPower The superpower of the <code>Superhero</code>.
     * @param oneThing   The one thing about the <code>Superhero</code>.
     */
    public Superhero(String userName, String name, String superPower, String oneThing)
    {
        mUserName = userName;
        mName = name;
        mSuperPower = superPower;
        mOneThing = oneThing;
        mFileName = mUserName + ".png";
    }

    /**
     * Gets the username of the <code>Superhero</code>.
     *
     * @return The username of the <code>Superhero</code>.
     */
    public String getUserName()
    {
        return mUserName;
    }

    /**
     * Gets the name of the <code>Superhero</code>.
     *
     * @return The name of the <code>Superhero</code>.
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Gets the superpower of the <code>Superhero</code>.
     *
     * @return The superpower of the <code>Superhero</code>.
     */
    public String getSuperPower()
    {
        return mSuperPower;
    }

    /**
     * Gets the one thing about the <code>Superhero</code>.
     *
     * @return The one thing about the <code>Superhero</code>.
     */
    public String getOneThing()
    {
        return mOneThing;
    }

    /**
     * Gets the file name of the <code>Superhero</code> with its path. For example: dtran258.png
     *
     * @return The file name of the <code>Superhero</code>.
     */
    public String getFileName()
    {
        return mFileName;
    }

    /**
     * Generates a text based representation of this <code>Superhero</code>.
     *
     * @return A text based representation of this <code>Superhero</code>.
     */
    @Override
    public String toString()
    {
        return "Superhero{" + "mUserName='" + mUserName + '\'' + ", mName='" + mName + '\''
                + ", mSuperPower='" + mSuperPower + '\'' + ", mOneThing='" + mOneThing + '\''
                + ", mFileName='" + mFileName + '\'' + '}';
    }
}
