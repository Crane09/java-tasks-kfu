public class step_9 {
    // 1. UntrustworthyMailWorker
public static class UntrustworthyMailWorker implements MailService {

    private final MailService[] services;
    private final RealMailService realMailService = new RealMailService();

    public UntrustworthyMailWorker(MailService[] services) {
        this.services = services;
    }

    public RealMailService getRealMailService() {
        return realMailService;
    }

    @Override
    public Sendable processMail(Sendable mail) {
        Sendable result = mail;
        for (MailService service : services) {
            result = service.processMail(result);
        }
        return realMailService.processMail(result);
    }
}

// 2. Spy
public static class Spy implements MailService {

    private final java.util.logging.Logger logger;

    public Spy(java.util.logging.Logger logger) {
        this.logger = logger;
    }

    @Override
    public Sendable processMail(Sendable mail) {
        if (mail instanceof MailMessage) {
            MailMessage message = (MailMessage) mail;
            if (Main.AUSTIN_POWERS.equals(message.getFrom())
                    || Main.AUSTIN_POWERS.equals(message.getTo())) {
                logger.warning(
                        "Detected target mail correspondence: from " +
                        message.getFrom() + " to " + message.getTo() +
                        " \"" + message.getMessage() + "\""
                );
            } else {
                logger.info(
                        "Usual correspondence: from " +
                        message.getFrom() + " to " + message.getTo()
                );
            }
        }
        return mail;
    }
}

// 3. Thief
public static class Thief implements MailService {

    private final int minPrice;
    private int stolenValue = 0;

    public Thief(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getStolenValue() {
        return stolenValue;
    }

    @Override
    public Sendable processMail(Sendable mail) {
        if (mail instanceof MailPackage) {
            MailPackage mp = (MailPackage) mail;
            Package pkg = mp.getContent();
            if (pkg.getPrice() >= minPrice) {
                stolenValue += pkg.getPrice();
                return new MailPackage(
                        mp.getFrom(),
                        mp.getTo(),
                        new Package("stones instead of " + pkg.getContent(), 0)
                );
            }
        }
        return mail;
    }
}

// 4. Inspector
public static class Inspector implements MailService {

    @Override
    public Sendable processMail(Sendable mail) {
        if (mail instanceof MailPackage) {
            Package pkg = ((MailPackage) mail).getContent();
            String content = pkg.getContent();

            if (Main.WEAPONS.equals(content) || Main.BANNED_SUBSTANCE.equals(content)) {
                throw new IllegalPackageException();
            }

            if (content.contains("stones")) {
                throw new StolenPackageException();
            }
        }
        return mail;
    }
}

// 5. Исключения
public static class IllegalPackageException extends RuntimeException {
}

public static class StolenPackageException extends RuntimeException {
}
}
