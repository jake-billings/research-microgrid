package edu.ucdenver.park.microgrid.live;

import edu.ucdenver.park.microgrid.data.MicrogridGraph;
import edu.ucdenver.park.microgrid.dummy.DummyMicrogrid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LiveMicrogridGraphTest {
    @Test
    void shouldStartWithEmptyState() {
        LiveMicrogridGraph empty = new LiveMicrogridGraph();

        MicrogridGraph state = empty.getCurrentState();

        assert state.getEdges().size() == 0;
        assert state.getNodes().size() == 0;
    }

    @Test
    void shouldAddDummyGraphToState() {
        LiveMicrogridGraph live = new LiveMicrogridGraph();
        MicrogridGraphMessage message = new MicrogridGraphMessage(new DummyMicrogrid(), System.currentTimeMillis()+60000);
        live.receiveMessage(message);
        MicrogridGraph state = live.getCurrentState();

        assert state.getEdges().size() > 0;
        assert state.getNodes().size() > 0;
    }

    @Test
    void shouldExpireDummyGraphState() {
        LiveMicrogridGraph live = new LiveMicrogridGraph();
        MicrogridGraphMessage message = new MicrogridGraphMessage(new DummyMicrogrid(), System.currentTimeMillis()-1000);
        live.receiveMessage(message);
        MicrogridGraph state = live.getCurrentState();

        assert state.getEdges().size() == 0;
        assert state.getNodes().size() == 0;
    }

}