import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from "@/components/ui/navigation-menu";

const navItems = [
  { label: "Features", href: "#features" },
  { label: "About", href: "#about" },
  { label: "FAQ", href: "#faq" },
] as const;

export function NavLinks() {
  const pathname = usePathname();
  const isHomePage = pathname === "/home";

  return (
    <NavigationMenu>
      <NavigationMenuList className="gap-2">
        {navItems
          .filter((item) => {
            if (isHomePage && (item.label === "About" || item.label === "FAQ" || item.label === "Features")) {
              return false;
            }
            return true;
          })
          .map((item) => (
            <NavigationMenuItem key={item.href}>
              <NavigationMenuLink
                asChild
                className="bg-transparent px-4 text-muted-foreground hover:text-foreground"
              >
                <Link href={item.href}>
                  {item.label}
                </Link>
              </NavigationMenuLink>
            </NavigationMenuItem>
          ))}
      </NavigationMenuList>
    </NavigationMenu>
  );
}
