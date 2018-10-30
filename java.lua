scriptId = 'com.thalmic.scripts.presentation'
scriptDetailsUrl = 'https://market.myo.com/app/5474c658e4b0361138df2a9e'
scriptTitle = 'Java'

function onForegroundWindowChange(app, title)
    local uppercaseApp = string.upper(app)
    myo.debug("onForegroundWindowChange: " .. app .. ", " .. title)
    return platform == "MacOS" and app == "com.microsoft.Powerpoint" or
        platform == "Windows" and (uppercaseApp == "JAVA.EXE")
end

function activeAppName()
    return "Java"
end

-- flag to de/activate shuttling feature
supportShuttle = false
fist = "down"

-- Effects

function forward()
    myo.keyboard("left_arrow", "up")
    myo.keyboard("right_arrow", "down")
end

function backward()
    myo.keyboard("right_arrow", "up")
    myo.keyboard("left_arrow", "down")
end

function jump()
    myo.keyboard("up_arrow", fist)
    if fist == "down" then
        fist = "up"
    else
        fist = "down"
    end
end

-- Helpers

function conditionallySwapWave(pose)
    if myo.getArm() == "left" then
        if pose == "waveIn" then
            pose = "waveOut"
        elseif pose == "waveOut" then
            pose = "waveIn"
        end
    end
    return pose
end

-- Shuttle

function shuttleBurst()
    if shuttleDirection == "forward" then
        forward()
    elseif shuttleDirection == "backward" then
        backward()
    elseif shuttleDirection == "jump" then
        jump()
    end
end

-- Triggers

function onPoseEdge(pose, edge)
    -- Forward/backward and shuttle
    --myo.setLockingPolicy("none")
    if pose == "waveIn" or pose == "waveOut"  or pose == "fingersSpread" or pose == "fist" then
        local now = myo.getTimeMilliseconds()

        if edge == "on" then
            -- Deal with direction and arm
            myo.setLockingPolicy("none")
            pose = conditionallySwapWave(pose)

            myo.debug(pose)
            if pose == "waveIn" or pose == "fist" then
                shuttleDirection = "backward"
            elseif pose == "waveOut" then
                shuttleDirection = "forward"
            else
                shuttleDirection = "jump"
            end

            -- Extend unlock and notify user
            myo.unlock("hold")
            myo.notifyUserAction()

            -- Initial burst
            shuttleBurst()
            shuttleSince = now
            shuttleTimeout = SHUTTLE_CONTINUOUS_TIMEOUT
        end
        -- if edge == "off" then
        --     myo.unlock("timed")
        --     shuttleTimeout = nil
        -- end
    end
end

-- All timeouts in milliseconds
SHUTTLE_CONTINUOUS_TIMEOUT = 600
SHUTTLE_CONTINUOUS_PERIOD = 300

function onPeriodic()
    local now = myo.getTimeMilliseconds()
    if supportShuttle and shuttleTimeout then
        if (now - shuttleSince) > shuttleTimeout then
            shuttleBurst()
            shuttleTimeout = SHUTTLE_CONTINUOUS_PERIOD
            shuttleSince = now
        end
    end
end