package com._7aske.grain.data.session;

import org.hibernate.Session;

public interface SessionProvider {
    Session getSession();
}
