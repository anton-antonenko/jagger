package com.griddynamics.jagger.user.test.configurations.load;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonList;

/**
 * This load is a list of user groups {@link JLoadProfileUsers}. Every such user group imitates a group of threads. Threads will start sequentially.
 * Thus you are able to create load ramp-up and rump-down with this load type. You can configure a number of threads by attributes of user group.<p>
 * Available attributes:<p>
 *     - numberOfUsers - A goal number of threads.<p>
 *     - lifeTimeInSeconds - Describes how long threads will be alive. Default is 2 days.<p>
 *     - startDelayInSeconds - Delay before first thread will start. Default is 0.<p>
 *     - slewRateUsersPerSecond - Describes how many threads to start during every iteration. Default is numberOfUsers value.<p>
 * You can set optional attribute delayBetweenInvocationsInSeconds to specify delay in seconds between invocations (default value is 0s).
 *
 * @ingroup Main_Load_profiles_group
 */
public class JLoadProfileUserGroups implements JLoadProfile {

    private final List<JLoadProfileUsers> userGroups;
    private final int delayBetweenInvocationsInSeconds;
    private final int tickInterval;

    private JLoadProfileUserGroups(Builder builder) {
        this.userGroups = builder.userGroups;
        this.delayBetweenInvocationsInSeconds = builder.delayBetweenInvocationsInSeconds;
        this.tickInterval = builder.tickInterval;
    }

    /** Builder of the JLoadProfileUserGroups
     * @n
     * @details Constructor parameters are mandatory for the JLoadProfileUserGroups. All parameters, set by setters are optional
     * @n
     * @param userGroup   - User group which Jagger will imitate
     */
    public static Builder builder(JLoadProfileUsers userGroup) {
        return new Builder(userGroup);
    }

    /** Builder of the JLoadProfileUserGroups
     * @n
     * @details Constructor parameters are mandatory for the JLoadProfileUserGroups. All parameters, set by setters are optional
     * @n
     * @param userGroup   - User group which Jagger will imitate
     * @param userGroups  - User groups which Jagger will imitate
     */
    public static Builder builder(JLoadProfileUsers userGroup, JLoadProfileUsers... userGroups) {
        return new Builder(userGroup, userGroups);
    }

    public static class Builder {
        static final int DEFAULT_TICK_INTERVAL = 1000;
        private final List<JLoadProfileUsers> userGroups;
        private int delayBetweenInvocationsInSeconds;

        // Tick interval doesn't have setter, since it's unclear if this field is needed. Check https://issues.griddynamics.net/browse/JFG-1000
        private int tickInterval;

        /** Builder of the JLoadProfileUserGroups
         * @n
         * @details Constructor parameters are mandatory for the JLoadProfileUserGroups. All parameters, set by setters are optional
         * @n
         * @param userGroup   - User group which Jagger will imitate
         */
        private Builder(JLoadProfileUsers userGroup) {
            Objects.requireNonNull(userGroup);
            this.userGroups = singletonList(userGroup);
            this.tickInterval = DEFAULT_TICK_INTERVAL;
        }

        /** Builder of the JLoadProfileUserGroups
         * @n
         * @details Constructor parameters are mandatory for the JLoadProfileUserGroups. All parameters, set by setters are optional
         * @n
         * @param userGroup   - User group which Jagger will imitate
         * @param userGroups  - User groups which Jagger will imitate
         */
        public Builder(JLoadProfileUsers userGroup, JLoadProfileUsers... userGroups) {
            Objects.requireNonNull(userGroup);
            ArrayList<JLoadProfileUsers> groups = new ArrayList<>();
            groups.add(userGroup);
            Collections.addAll(groups, userGroups);
            this.userGroups = groups;
            this.tickInterval = DEFAULT_TICK_INTERVAL;
        }


        /** Creates an object of JLoadProfileUserGroups type with custom parameters.
         * @return JLoadProfileUserGroups object.
         */
        public JLoadProfileUserGroups build() {
            return new JLoadProfileUserGroups(this);
        }

        /**
         * Optional: Delay between invocations in seconds. Default is 0 s.
         * @param delayBetweenInvocationsInSeconds Delay between invocations in seconds
         */
        public Builder withDelayBetweenInvocationsInSeconds(int delayBetweenInvocationsInSeconds) {
            if (delayBetweenInvocationsInSeconds < 0) {
                throw new IllegalArgumentException(
                        String.format("Delay between invocations must be >= 0. Provided value is %s", delayBetweenInvocationsInSeconds));
            }
            this.delayBetweenInvocationsInSeconds = delayBetweenInvocationsInSeconds;
            return this;
        }
    }

    public List<JLoadProfileUsers> getUserGroups() {
        return userGroups;
    }

    public int getDelayBetweenInvocationsInSeconds() {
        return delayBetweenInvocationsInSeconds;
    }

    public int getTickInterval() {
        return tickInterval;
    }
}
